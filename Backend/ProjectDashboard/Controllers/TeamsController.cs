using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

using ProjectDashboard.DTO;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;

using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;

namespace ProjectDashboard.Controllers {

	[ApiConventionType(typeof(DefaultApiConventions))]
	[Produces("application/json")]
	[Route("api/[controller]")]
	[ApiController]
	[Authorize]
	public class TeamsController : ControllerBase {

		private readonly IBaseRepository<Team> _teamRepo;
		private readonly IBaseRepository<User> _userRepo;

		public TeamsController(IBaseRepository<Team> _teamRepo, IBaseRepository<User> userRepo) {
			this._teamRepo = _teamRepo;
			this._userRepo = userRepo;
		}

		[HttpGet]
		public IEnumerable<TeamDTO> GetAll() {
			int userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier).Value);

			return _teamRepo
				.GetAll()
				.Where(x => x.LeadId == userId || x.Members.Any(y => y.MemberId == userId))
				.Select(x => new TeamDTO {
					Id = x.Id,
					Name = x.Name,
					LeadId = x.LeadId,
					MemberIds = x.Members.Select(y => y.MemberId)
				});
		}

		[HttpGet("{id}")]
		public ActionResult<TeamDTO> GetById(int id) {
			Team item = _teamRepo.GetById(id);
			if (item == null)
				return NotFound();

			return new TeamDTO {
				Id = item.Id,
				Name = item.Name,
				LeadId = item.LeadId,
				MemberIds = item.Members.Select(x => x.MemberId)
			};
		}

		[HttpPost]
		public IActionResult Post(TeamDTO model) {
			Team item = new Team();
			model.UpdateFromModel(item);

			foreach (int id in model.MemberIds) {
				User member = _userRepo.GetById(id);
				item.Members.Add(new TeamMember { Team = item, Member = member });
			}

			_teamRepo.Add(item);
			_teamRepo.SaveChanges();

			TeamDTO dto = new TeamDTO {
				Id = item.Id,
				Name = item.Name,
				LeadId = item.LeadId,
				MemberIds = item.Members.Select(x => x.MemberId)
			};

			return CreatedAtAction(nameof(GetAll), dto);
		}

		[HttpPut("{id}")]
		public IActionResult Put(int id, TeamDTO model) {
			Team item = _teamRepo.GetById(id);
			if (item == null)
				return NotFound();

			model.UpdateFromModel(item);

			item.Members.Clear();
			foreach (int memberId in model.MemberIds) {
				User member = _userRepo.GetById(memberId);
				item.Members.Add(new TeamMember { Team = item, Member = member });
			}

			_teamRepo.SaveChanges();

			return NoContent();
		}

		[HttpDelete("{id}")]
		public IActionResult Delete(int id) {
			Team item = _teamRepo.GetById(id);
			if (item == null)
				return NotFound();

			_teamRepo.Remove(item);
			_teamRepo.SaveChanges();

			TeamDTO dto = new TeamDTO {
				Id = item.Id,
				Name = item.Name,
				LeadId = item.LeadId,
				MemberIds = item.Members.Select(x => x.MemberId)
			};

			return Ok(dto);
		}
	}
}