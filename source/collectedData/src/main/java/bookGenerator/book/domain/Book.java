package bookGenerator.book.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import bookGenerator.BootApplication;
import bookGenerator._global.infra.LoggedEntity;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "App_Book")
public class Book extends LoggedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private Long bookId;

	private Long createrId;

	private Long coverImageFileId;

	private String title;

	private Long likes;

	private Boolean isShared;

    private Date createdDate;
    
    private Date updatedDate;


    public static BookRepository repository() {
        return BootApplication.applicationContext.getBean(
            BookRepository.class
        );
    }


    @PrePersist
    public void onPrePersist() {
        this.createdDate = new Date();
        this.updatedDate = new Date();

        super.onPrePersist();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedDate = new Date();

        super.onPreUpdate();
    }
}
