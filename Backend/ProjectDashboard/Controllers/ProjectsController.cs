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
	public class ProjectsController : ControllerBase {

		private readonly IBaseRepository<Project> _projectRepo;

		public ProjectsController(IBaseRepository<Project> projectRepo) {
			this._projectRepo = projectRepo;
		}

		[HttpGet]
		public IEnumerable<Project> GetAll() {
			return _projectRepo.GetAll();
		}

		[HttpGet("{id}")]
		public ActionResult<Project> GetById(int id) {
			Project item = _projectRepo.GetById(id);
			if (item == null)
				return NotFound();

			return item;
		}

		[HttpPost]
		public IActionResult Post(ProjectDTO model) {
			Project item = new Project();
			model.UpdateFromModel(item);

			_projectRepo.Add(item);
			_projectRepo.SaveChanges();

			return CreatedAtAction(nameof(GetAll), item);
		}

		[HttpPut("{id}")]
		public IActionResult Put(int id, ProjectDTO model) {
			Project item = _projectRepo.GetById(id);
			if (item == null)
				return NotFound();

			model.UpdateFromModel(item);

			_projectRepo.Add(item);
			_projectRepo.SaveChanges();

			return NoContent();
		}

		[HttpDelete("{id}")]
		public IActionResult Delete(int id) {
			Project item = _projectRepo.GetById(id);
			if (item == null)
				return NotFound();

			_projectRepo.Remove(item);
			_projectRepo.SaveChanges();

			return Ok(item);
		}
	}
}