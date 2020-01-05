using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using ProjectDashboard.DTO;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;

using System.Collections.Generic;
using System.Linq;

namespace ProjectDashboard.Controllers {

	[ApiConventionType(typeof(DefaultApiConventions))]
	[Produces("application/json")]
	[Route("api/[controller]")]
	[ApiController]
	[Authorize]
	public class TasksController : ControllerBase {

		private readonly IBaseRepository<ProjectTask> _taskRepo;

		public TasksController(IBaseRepository<ProjectTask> taskRepo) {
			this._taskRepo = taskRepo;
		}

		[HttpGet]
		[Authorize(Policy = "Admin")]
		public IEnumerable<ProjectTaskDTO> GetAll() {
			return _taskRepo
				.GetAll()
				.Select(x => new ProjectTaskDTO {
					Id = x.Id,
					Title = x.Title,
					Description = x.Description,
					IsFinished = x.IsFinished,
					ProjectId = x.ProjectId,
					AssigneeId = x.AssigneeId
				});
		}

		[HttpGet("{id}")]
		public ActionResult<ProjectTaskDTO> GetById(int id) {
			ProjectTask item = _taskRepo.GetById(id);
			if (item == null)
				return NotFound();

			var dto = new ProjectTaskDTO {
				Id = item.Id,
				Title = item.Title,
				Description = item.Description,
				IsFinished = item.IsFinished,
				ProjectId = item.ProjectId,
				AssigneeId = item.AssigneeId
			};

			return dto;
		}

		[HttpPost]
		public IActionResult Post(ProjectTaskDTO model) {
			ProjectTask item = new ProjectTask();
			model.UpdateFromModel(item);

			_taskRepo.Add(item);
			_taskRepo.SaveChanges();

			var dto = new ProjectTaskDTO {
				Id = item.Id,
				Title = item.Title,
				Description = item.Description,
				IsFinished = item.IsFinished,
				ProjectId = item.ProjectId,
				AssigneeId = item.AssigneeId
			};

			return CreatedAtAction(nameof(GetAll), dto);
		}

		[HttpPut("{id}")]
		public IActionResult Put(int id, ProjectTaskDTO model) {
			ProjectTask item = _taskRepo.GetById(id);
			if (item == null)
				return NotFound();

			model.UpdateFromModel(item);

			_taskRepo.Add(item);
			_taskRepo.SaveChanges();

			return NoContent();
		}

		[HttpDelete("{id}")]
		public IActionResult Delete(int id) {
			ProjectTask item = _taskRepo.GetById(id);
			if (item == null)
				return NotFound();

			_taskRepo.Remove(item);
			_taskRepo.SaveChanges();

			var dto = new ProjectTaskDTO {
				Id = item.Id,
				Title = item.Title,
				Description = item.Description,
				IsFinished = item.IsFinished,
				ProjectId = item.ProjectId,
				AssigneeId = item.AssigneeId
			};

			return Ok(dto);
		}
	}
}