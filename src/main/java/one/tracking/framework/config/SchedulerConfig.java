/**
 *
 */
package one.tracking.framework.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import one.tracking.framework.domain.PushNotificationRequest;
import one.tracking.framework.entity.DeviceToken;
import one.tracking.framework.entity.SurveyInstance;
import one.tracking.framework.entity.meta.ReleaseStatusType;
import one.tracking.framework.entity.meta.ReminderType;
import one.tracking.framework.entity.meta.Survey;
import one.tracking.framework.repo.DeviceTokenRepository;
import one.tracking.framework.repo.SurveyInstanceRepository;
import one.tracking.framework.repo.SurveyRepository;
import one.tracking.framework.repo.SurveyResponseRepository;
import one.tracking.framework.service.FirebaseService;
import one.tracking.framework.service.SurveyService;

/**
 * @author Marko Voß
 *
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

  private static final Logger LOG = LoggerFactory.getLogger(SchedulerConfig.class);

  private static final String TASK_SEND_REMINDER = "REMINDER";

  @Autowired
  private DeviceTokenRepository deviceTokenRepository;

  @Autowired
  private SurveyResponseRepository surveyResponseRepository;

  @Autowired
  private SurveyInstanceRepository surveyInstanceRepository;

  @Autowired
  private SurveyService surveyService;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private FirebaseService firebaseService;

  @Autowired
  private Locker locker;

  @Value("${app.timeout.reminder.lock}")
  private Integer timeoutReminderLock;

  @Value("${app.reminder.title}")
  private String reminderTitle;

  @Value("${app.reminder.message}")
  private String reminderMessage;

  @Scheduled(cron = "0 0 12 * * *") // Run job every day @ 12am
  public void sendReminder() {

    LOG.info("Executing scheduled job '{}' @ {}", TASK_SEND_REMINDER, Instant.now());

    try {

      if (lockAndSendReminder())
        LOG.info("Executing scheduled job '{}' DONE @ {}", TASK_SEND_REMINDER, Instant.now());
      else
        LOG.info("Executing scheduled job '{}' CANCELLED @ {}", TASK_SEND_REMINDER, Instant.now());

    } catch (final Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  private boolean lockAndSendReminder() {

    try {

      this.locker.lock(TASK_SEND_REMINDER, this.timeoutReminderLock);

      performSendReminder();
      return true;

    } catch (final DataIntegrityViolationException e) {
      // In case of concurrency by multiple instances, failing to store the same entry is valid
      // Unique index or primary key violation is to be expected
      LOG.info("Expected violation: {}", e.getMessage());
      return false;
    }
  }

  private void performSendReminder() {

    LOG.info("Sending reminders...");

    final Instant now = Instant.now();

    final List<String> handledNameIds = new ArrayList<>();

    final List<Survey> surveys =
        this.surveyRepository.findAllByReleaseStatusAndReminderTypeNotOrderByNameIdAscVersionDesc(
            ReleaseStatusType.RELEASED,
            ReminderType.NONE);

    for (final Survey survey : surveys) {

      // Handle the current version only
      if (handledNameIds.contains(survey.getNameId()))
        continue;

      handledNameIds.add(survey.getNameId());

      final ChronoUnit unit = getChronoUnit(survey.getReminderType());

      if (unit == null) {
        LOG.error("No mapping defined for reminder type: {}! Skipping sending reminders for survey: {}.",
            survey.getReminderType(), survey.getNameId());
        continue;
      }

      final SurveyInstance instance = this.surveyService.getCurrentInstance(survey);

      if (now.isAfter(instance.getStartTime().plus(survey.getReminderValue(), unit))) {

        performSendReminder(survey, instance);
      }
    }
  }

  /**
   * @param survey
   * @param instance
   */
  private void performSendReminder(final Survey survey, final SurveyInstance instance) {

    for (final DeviceToken deviceToken : this.deviceTokenRepository.findAll()) {

      if (this.surveyResponseRepository.existsByUserAndSurveyInstance(deviceToken.getUser(), instance))
        continue;

      try {

        this.firebaseService.sendMessageToUser(PushNotificationRequest.builder()
            .title(this.reminderTitle)
            .message(this.reminderMessage)
            .data(Collections.singletonMap("surveyNameId", survey.getNameId()))
            .build());

      } catch (InterruptedException | ExecutionException e) {
        LOG.error(e.getMessage(), e);
      }
    }
  }

  private ChronoUnit getChronoUnit(final ReminderType type) {

    switch (type) {
      case AFTER_DAYS:
        return ChronoUnit.DAYS;
      default:
        return null;
    }
  }

}
