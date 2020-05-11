/**
 *
 */
package one.tracking.framework.dto;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * TODO: Currently we do not support text answers.
 *
 * @author Marko Voß
 *
 */
@Data
@Builder
@ApiModel
public class SurveyResponseDto {

  @NotNull
  private Long questionId;

  private List<Long> answerIds;

  private Boolean boolAnswer;

  private String textAnswer;

  private Integer rangeAnswer;

  private Map<Long, Boolean> checklistAnswer;
}
