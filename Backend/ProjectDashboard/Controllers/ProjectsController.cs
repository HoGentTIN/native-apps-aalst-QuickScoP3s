using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using ProjectDashboard.DTO;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;

namespace ProjectDashboard.Controllers {

	[ApiConventionType(typeof(DefaultApiConventions))]
	[Produces("application/json")]
	[Route("api/[controller]")]
	[ApiController]
	[Authorize]
	public class ProjectsController : ControllerBase {

		private readonly IProjectRepository _projectRepo;

		public ProjectsController(IProjectRepository projectRepo) {
			this._projectRepo = projectRepo;
		}

		[HttpGet]
		public IEnumerable<Project> GetAll() {
			var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier).Value);
			return _projectRepo.GetAll().Where(x => x.Team.LeadId == userId || x.Team.Members.Any(y => y.MemberId == userId));
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

			item.LastEdit = DateTime.Now;

			if (_projectRepo.NameExists(item.Name))
				return Conflict("A project with this name already exists");

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
			item.LastEdit = DateTime.Now;

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