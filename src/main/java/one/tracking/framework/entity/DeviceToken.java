/**
 *
 */
package one.tracking.framework.entity;

import static one.tracking.framework.entity.DataConstants.TOKEN_DEVICE_MAX_LENGTH;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    indexes = {
        @Index(name = "IDX_DEVICE_TOKEN", columnList = "token"),
    })
@NamedQueries({
    @NamedQuery(name = "DeviceToken.findByCreatedAtBefore", query = "SELECT d FROM DeviceToken d "
        + "WHERE d.createdAt < ?1 "
        + "ORDER BY d.id ASC"),
    @NamedQuery(name = "DeviceToken.deleteById", query = "DELETE FROM DeviceToken d WHERE d.id = ?1")
})
public class DeviceToken {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, length = TOKEN_DEVICE_MAX_LENGTH, unique = true)
  private String token;

  @ManyToOne(optional = false)
  private User user;

  @Column(nullable = false)
  private Instant createdAt;

  @PrePersist
  void onPrePersist() {
    if (this.id == null) {
      setCreatedAt(Instant.now());
    }
  }
}
