package pl.cba.gibcode.apiQuery.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("api")
@RequiredArgsConstructor
public class BrandQueryController {


	@ApiOperation(
			value = "Get all brands for admin",
			notes = "This call is used to get all brands filtered",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Brands returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("admin/brands")
	public ResponseEntity<Page<BrandResponseDto>> getBrandBy(Pageable pageable, @RequestBody BrandCriteriaDto criteria, @RequestParam String username) {
		//pretend validation for admin
		userService.loginAsAdmin(username);
		return ResponseEntity.ok(brandService.findAllForAdmin(pageable, criteria));
	}



	@ApiOperation(
			value = "Get all brands for sale",
			notes = "This call is used to get all brands filtered",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Brands returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("brands/list")
	public ResponseEntity<Page<BrandResponseDto>> getBrandBy(Pageable pageable, @RequestBody BrandCriteriaDto criteria) {
		return ResponseEntity.ok(brandService.findAll(pageable, criteria));
	}

}
