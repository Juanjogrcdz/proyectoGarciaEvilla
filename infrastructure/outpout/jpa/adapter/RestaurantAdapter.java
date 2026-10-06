import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;

    @Override
    public List<Restaurant> findAllOrderedByName(int page, int size) {
        // ignoreCase: "bar" y "Zeta" se ordenan alfabéticamente sin importar mayúsculas
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name").ignoreCase()));

        return restaurantRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(restaurantEntityMapper::toModel)   // usa el mismo método que ya usas en findById
                .toList();
    }