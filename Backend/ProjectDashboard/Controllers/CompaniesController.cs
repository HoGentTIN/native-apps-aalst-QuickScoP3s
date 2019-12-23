using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using ProjectDashboard.DTO;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;

namespace ProjectDashboard.Controllers {

	[ApiConventionType(typeof(DefaultApiConventions))]
	[Produces("application/json")]
	[Route("api/[controller]")]
	[ApiController]
	public class CompaniesController : ControllerBase {

		private readonly IBaseRepository<Company> _companyRepo;

		public CompaniesController(IBaseRepository<Company> companyRepo) {
			this._companyRepo = companyRepo;
		}

		[HttpGet]
		public IEnumerable<Company> GetAll() {
			return _companyRepo.GetAll();
		}

		[HttpPost]
		public IActionResult PostCompany(CompanyDTO model) {
			Company company = new Company();
			model.UpdateFromModel(company);

			_companyRepo.Add(company);
			_companyRepo.SaveChanges();

			return CreatedAtAction(nameof(GetAll), company);
		}
	}
}