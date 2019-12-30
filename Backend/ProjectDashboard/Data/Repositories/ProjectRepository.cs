using Microsoft.EntityFrameworkCore;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectDashboard.Data.Repositories {
	public class ProjectRepository : IBaseRepository<Project> {

		private readonly ApplicationDbContext _context;
		private readonly DbSet<Project> _projects;

		public ProjectRepository(ApplicationDbContext context) {
			this._context = context;
			this._projects = context.Projects;
		}

		public IEnumerable<Project> GetAll() {
			return _projects.AsNoTracking().ToList();
		}

		public Project GetById(int id) {
			return _projects.SingleOrDefault(x => x.Id == id);
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
