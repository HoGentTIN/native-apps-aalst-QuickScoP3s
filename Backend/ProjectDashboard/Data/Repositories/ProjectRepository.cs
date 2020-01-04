using Microsoft.EntityFrameworkCore;
using ProjectDashboard.Extensions;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;
using System.Collections.Generic;
using System.Linq;

namespace ProjectDashboard.Data.Repositories {
	public class ProjectRepository : IProjectRepository {

		private readonly ApplicationDbContext _context;
		private readonly DbSet<Project> _projects;

		public ProjectRepository(ApplicationDbContext context) {
			this._context = context;
			this._projects = context.Projects;
		}

		public IEnumerable<Project> GetAll() {
			return _projects.AsNoTracking()
				.Include(x => x.Owner)
				.Include(x => x.Team).ThenInclude(x => x.Lead)
				.Include(x => x.Team).ThenInclude(x => x.Members).ThenInclude(x => x.Member)
				.Include(x => x.Tasks).ThenInclude(x => x.Assignee)
				.ToList();
		}

		public Project GetById(int id) {
			return _projects
				.Include(x => x.Owner)
				.Include(x => x.Team).ThenInclude(x => x.Lead)
				.Include(x => x.Team).ThenInclude(x => x.Members).ThenInclude(x => x.Member)
				.Include(x => x.Tasks).ThenInclude(x => x.Assignee)
				.SingleOrDefault(x => x.Id == id);
		}

		public bool NameExists(int ownerId, string name) {
			return _projects.Where(x => x.OwnerId == ownerId).Any(x => x.Name.EqualsIgnoreCase(name));
		}

		public void Add(Project item) {
			_projects.Add(item);
		}

		public void Remove(Project item) {
			_projects.Remove(item);
		}

		public void SaveChanges() {
			_context.SaveChanges();
		}
	}
}
