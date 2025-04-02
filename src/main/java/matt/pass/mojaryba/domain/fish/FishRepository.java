package matt.pass.mojaryba.domain.fish;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishRepository extends JpaRepository<Fish, Long> {
     Page<Fish> findAllByFishType_Name(String FishTypeName, Pageable pageable);
     List<Fish> findAllByUser_Email(String userEmail);
     List<Fish> findAllByFishType_NameAndUser_Email(String fishTypeName, String UserEmail);
     List<Fish> findAllByLikesIsNotNull();
     List<Fish> findAllByWeightGreaterThan(double weight);
     List<Fish> findAllByUser_Nick(String nick);

}
