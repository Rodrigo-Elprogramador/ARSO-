package arso.productos.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import arso.productos.modelo.ProductoDTO;

@Component
public class ProductoDTOAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>> {

    @Override
    public EntityModel<ProductoDTO> toModel(ProductoDTO productoDTO) {
        try {
            EntityModel<ProductoDTO> resultado = EntityModel.of(productoDTO, 
                    linkTo(methodOn(ProductoRestController.class).getProducto(productoDTO.getId())).withSelfRel());
            return resultado;
        } catch(Exception e) {
            return EntityModel.of(productoDTO);
        }
    }
}