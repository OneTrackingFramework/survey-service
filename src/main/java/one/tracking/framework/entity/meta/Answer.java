/**
 *
 */
package one.tracking.framework.entity.meta;

import java.time.Instant;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Marko Voß
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Answer {

  @Id
  @GeneratedValue
  private Long id;

  @Column(length = 255)
  private String value;

  @Column(nullable = false, updatable = false)
  private Instant timestampCreate;

  @Column(nullable = false, updatable = false)
  private Integer timestampCreateOffset;

  @PrePersist
  void onPrePersist() {
    if (this.id == null) {
      final OffsetDateTime timestamp = OffsetDateTime.now();
      setTimestampCreate(timestamp.toInstant());
      setTimestampCreateOffset(timestamp.getOffset().getTotalSeconds());
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Answer other = (Answer) obj;
    if (this.id != other.id)
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (this.id ^ (this.id >>> 32));
    return result;
  }
}
