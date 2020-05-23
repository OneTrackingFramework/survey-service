/**
 *
 */
package one.tracking.framework.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import one.tracking.framework.entity.meta.IntervalType;
import one.tracking.framework.entity.meta.ReleaseStatusType;
import one.tracking.framework.entity.meta.ReminderType;
import one.tracking.framework.entity.meta.Survey;
import one.tracking.framework.entity.meta.question.Question;

/**
 * @author Marko Voß
 *
 */
public interface SurveyRepository extends CrudRepository<Survey, Long> {

  List<Survey> findByNameId(String nameId);

  Optional<Survey> findByNameIdAndQuestionsIn(String nameId, Set<Question> questions);

  Optional<Survey> findTopByNameIdAndReleaseStatusOrderByVersionDesc(String nameId, ReleaseStatusType status);

  List<Survey> findAllByReleaseStatusOrderByNameIdAscVersionDesc(ReleaseStatusType status);

  List<Survey> findByNameIdOrderByVersionDesc(String nameId);

  List<Survey> findAllByReleaseStatusAndReminderTypeNotAndIntervalTypeNotOrderByNameIdAscVersionDesc(
      ReleaseStatusType status,
      ReminderType reminderType,
      IntervalType intervalType);

  List<Survey> findAllByNameIdAndReleaseStatusAndReminderTypeNotAndIntervalTypeNotOrderByNameIdAscVersionDesc(
      String nameId,
      ReleaseStatusType status,
      ReminderType reminderType,
      IntervalType intervalType);
}
