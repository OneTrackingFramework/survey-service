/**
 *
 */
package one.tracking.framework.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
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
public class SurveyResponseDto {

  @NotNull
  private Long questionId;

  private List<Long> answerIds;

  private Boolean boolAnswer;

  private String textAnswer;

  private Integer rangeAnswer;
}
