package org.ujar.loremipsum.history.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Report.TABLE_NAME)
public class Report {

  protected static final String TABLE_NAME = "loremipsum_reports";

  @Id
  @SequenceGenerator(
      name = "report_id_seq",
      sequenceName = "report_id_seq"
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "report_id_seq"
  )
  @JsonIgnore
  private Long id;

  @Column(name = "most_frequent_word")
  @JsonProperty("freq_word")
  private String mostFrequentWord;

  @Column(name = "avg_paragraph_size")
  @JsonProperty("avg_paragraph_size")
  private int avgParagraphSize;

  @Column(name = "avg_paragraph_processing_time")
  @JsonProperty("avg_paragraph_processing_time")
  private int avgParagraphProcessingTime;

  @Column(name = "total_processing_time")
  @JsonProperty("total_processing_time")
  private int totalProcessingTime;

  @CreatedDate
  @Column(name = "created_at")
  @JsonProperty("created_at")
  private Instant createdAt;

  public Report(String mostFrequentWord, int avgParagraphSize, int avgParagraphProcessingTime,
                int totalProcessingTime) {
    this.mostFrequentWord = mostFrequentWord;
    this.avgParagraphSize = avgParagraphSize;
    this.avgParagraphProcessingTime = avgParagraphProcessingTime;
    this.totalProcessingTime = totalProcessingTime;
  }
}
