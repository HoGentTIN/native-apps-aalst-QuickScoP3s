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
		public IEnumerable<ProjectDTO> GetAll() {
			int userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier).Value);

			return _projectRepo
				.GetAll()
				.Where(x => x.Team.LeadId == userId || x.Team.Members.Any(y => y.MemberId == userId))
				.Select(x => new ProjectDTO {
					Id = x.Id,
					Name = x.Name,
					TeamId = x.TeamId,
					OwnerId = x.OwnerId,
					LastEdit = x.LastEdit,
					ContactPerson = x.ContactPerson
				});
		}

		[HttpGet("{id}")]
		public ActionResult<ProjectDTO> GetById(int id) {
			Project item = _projectRepo.GetById(id);
			if (item == null)
				return NotFound();

			return new ProjectDTO {
				Id = item.Id,
				Name = item.Name,
				TeamId = item.TeamId,
				OwnerId = item.OwnerId,
				LastEdit = item.LastEdit,
				ContactPerson = item.ContactPerson
			};
		}

		[HttpPost]
		public IActionResult Post(ProjectDTO model) {
			Project item = new Project();
			model.UpdateFromModel(item);

			item.LastEdit = DateTime.Now;

			if (_projectRepo.NameExists(item.OwnerId, item.Name))
				return Conflict("A project with this name already exists for that owner");

			_projectRepo.Add(item);
			_projectRepo.SaveChanges();

			ProjectDTO dto = new ProjectDTO {
				Id = item.Id,
				Name = item.Name,
				TeamId = item.TeamId,
				OwnerId = item.OwnerId,
				LastEdit = item.LastEdit,
				ContactPerson = item.ContactPerson
			};

			return CreatedAtAction(nameof(GetAll), dto);
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

			ProjectDTO dto = new ProjectDTO {
				Id = item.Id,
				Name = item.Name,
				TeamId = item.TeamId,
				OwnerId = item.OwnerId,
				LastEdit = item.LastEdit,
				ContactPerson = item.ContactPerson
			};

			return Ok(dto);
		}
	}
}