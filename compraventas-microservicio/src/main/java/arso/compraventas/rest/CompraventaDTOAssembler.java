package arso.compraventas.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CompraventaDTOAssembler implements RepresentationModelAssembler<CompraventaDTO, EntityModel<CompraventaDTO>> {

    @Override
    public EntityModel<CompraventaDTO> toModel(CompraventaDTO dto) {
        try {
            return EntityModel.of(dto,
                linkTo(methodOn(CompraventaRestController.class).getCompraventa(dto.getId())).withSelfRel());
        } catch (Exception e) {
            return EntityModel.of(dto);
        }
    }
}