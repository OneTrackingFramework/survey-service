/**
 *
 */
package one.tracking.framework.entity.meta.question;

import one.tracking.framework.dto.meta.question.BooleanQuestionDto;
import one.tracking.framework.dto.meta.question.ChecklistEntryDto;
import one.tracking.framework.dto.meta.question.ChecklistQuestionDto;
import one.tracking.framework.dto.meta.question.ChoiceQuestionDto;
import one.tracking.framework.dto.meta.question.QuestionDto;
import one.tracking.framework.dto.meta.question.RangeQuestionDto;
import one.tracking.framework.dto.meta.question.TextQuestionDto;

/**
 * @author Marko Voß
 *
 */
public enum QuestionType {

  CHOICE,
  BOOL,
  RANGE,
  TEXT,
  CHECKLIST,
  CHECKLIST_ENTRY;

  public static QuestionType valueOf(final Class<? extends QuestionDto> clazz) {

    if (BooleanQuestionDto.class.isAssignableFrom(clazz))
      return BOOL;
    if (ChecklistQuestionDto.class.isAssignableFrom(clazz))
      return CHECKLIST;
    if (ChoiceQuestionDto.class.isAssignableFrom(clazz))
      return CHOICE;
    if (RangeQuestionDto.class.isAssignableFrom(clazz))
      return RANGE;
    if (TextQuestionDto.class.isAssignableFrom(clazz))
      return TEXT;
    if (ChecklistEntryDto.class.isAssignableFrom(clazz))
      return CHECKLIST_ENTRY;

    return null;
  }
}
