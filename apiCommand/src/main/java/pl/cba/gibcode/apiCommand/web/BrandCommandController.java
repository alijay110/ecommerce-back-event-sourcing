package pl.cba.gibcode.apiCommand.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cba.gibcode.apiCommand.service.BrandService;
import pl.cba.gibcode.apiCommand.service.UserServiceDelegate;
import pl.cba.gibcode.modelLibrary.brand.BasicBrandBody;
import pl.cba.gibcode.modelLibrary.brand.CreateBrandBody;
import pl.cba.gibcode.modelLibrary.brand.DeleteBrandBody;
import pl.cba.gibcode.modelLibrary.brand.UpdateBrandBody;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBody;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBodyFragment;
import pl.cba.gibcode.modelLibrary.model.UserType;

import javax.validation.Valid;

@RestController
@Api
@RequestMapping("api")
@RequiredArgsConstructor
public class BrandCommandController {

	private final UserServiceDelegate userServiceDelegate;
	private final BrandService brandService;

	@ApiOperation(
			value = "Create new brand",
			notes = "This call is used to create a new nonactive brand",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Created brand returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("brands/create")
	public ResponseEntity<BasicBrandBody> createBrand(
			@Valid @RequestBody CreateBrandBody createBrandDto,
			@RequestParam String username) {

		return ResponseEntity.status(HttpStatus.CREATED).body(brandService
				.createBrand(createBrandDto, userServiceDelegate.login(username, UserType.SELLER)));
	}

	@ApiOperation(
			value = "Update brand by admin",
			notes = "This call is used to update a brand",
			httpMethod = "PUT")
	@ApiResponses({
			@ApiResponse(code = 202, message = "Updated Brand returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PutMapping("admin/brands")
	public ResponseEntity<CreatedOrderUuidBody> updateBrand(@RequestBody UpdateBrandBody dto, @RequestParam String username) {
		var response = brandService.updateBrand(dto, userServiceDelegate.login(username, UserType.ADMIN));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@ApiOperation(
			value = "Delete brand by admin",
			notes = "This call is used to delete a brand",
			httpMethod = "DELETE")
	@ApiResponses({
			@ApiResponse(code = 202, message = "Deleted Brand returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@DeleteMapping("admin/brands")
	public ResponseEntity<CreatedOrderUuidBody> deleteBrand(@RequestBody DeleteBrandBody dto, @RequestParam String username) {
		var response = brandService.deleteBrand(dto, userServiceDelegate.login(username, UserType.ADMIN));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

}
