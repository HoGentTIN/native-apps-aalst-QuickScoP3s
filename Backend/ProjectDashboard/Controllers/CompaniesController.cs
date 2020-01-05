using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using ProjectDashboard.DTO;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;

using System.Collections.Generic;

namespace ProjectDashboard.Controllers {

	[ApiConventionType(typeof(DefaultApiConventions))]
	[Produces("application/json")]
	[Route("api/[controller]")]
	[ApiController]
	[Authorize]
	public class CompaniesController : ControllerBase {

		private readonly ICompanyRepository _companyRepo;

		public CompaniesController(ICompanyRepository companyRepo) {
			this._companyRepo = companyRepo;
		}

		[HttpGet]
		public IEnumerable<Company> GetAll() {
			return _companyRepo.GetAll();
		}

		[HttpGet("{id}")]
		public ActionResult<Company> GetById(int id) {
			Company item = _companyRepo.GetById(id);
			if (item == null)
				return NotFound();

			return item;
		}

		[HttpGet("{id}/users")]
		public IEnumerable<User> GetUsersFromCompany(int id) {
			return _companyRepo.GetUsersFromCompany(id);
		}

		[HttpPost]
		[Authorize(Policy = "CompanyAdmin")]
		public IActionResult Post(CompanyDTO model) {
			Company item = new Company();
			model.UpdateFromModel(item);

			_companyRepo.Add(item);
			_companyRepo.SaveChanges();

			return CreatedAtAction(nameof(GetAll), item);
		}

		[HttpPut("{id}")]
		[Authorize(Policy = "CompanyAdmin")]
		public IActionResult Put(int id, CompanyDTO model) {
			Company item = _companyRepo.GetById(id);
			if (item == null)
				return NotFound();

			model.UpdateFromModel(item);
			_companyRepo.SaveChanges();

			return NoContent();
		}

		[HttpDelete("{id}")]
		[Authorize(Policy = "CompanyAdmin")]
		public IActionResult Delete(int id) {
			Company item = _companyRepo.GetById(id);
			if (item == null)
				return NotFound();

			_companyRepo.Remove(item);
			_companyRepo.SaveChanges();

			return Ok(item);
		}
	}
}