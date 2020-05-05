/**
 *
 */
package one.tracking.framework.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import one.tracking.framework.entity.SurveyResponse;
import one.tracking.framework.entity.User;
import one.tracking.framework.entity.meta.Survey;
import one.tracking.framework.entity.meta.question.Question;

/**
 * @author Marko Voß
 *
 */
public interface SurveyResponseRepository extends CrudRepository<SurveyResponse, Long> {

  // Optional<SurveyResponse> findByUserIdAndSurveyIdAndQuestionId(String userId, Long surveyId, Long
  // questionId);

  List<SurveyResponse> findByUserAndSurveyAndQuestion(User user, Survey survey, Question question);

  void deleteByUserAndSurveyAndQuestion(User user, Survey survey, Question question);
}
