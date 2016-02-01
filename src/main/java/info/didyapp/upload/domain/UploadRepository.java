package info.didyapp.upload.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author Didy
 */
@Component
public interface UploadRepository extends PagingAndSortingRepository<Upload, String> {


}
