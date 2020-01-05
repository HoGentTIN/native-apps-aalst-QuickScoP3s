using Microsoft.EntityFrameworkCore;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;
using System.Collections.Generic;
using System.Linq;

namespace ProjectDashboard.Data.Repositories {
	public class TasksRepository : IBaseRepository<ProjectTask> {

		private readonly ApplicationDbContext _context;
		private readonly DbSet<ProjectTask> _tasks;

		public TasksRepository(ApplicationDbContext context) {
			this._context = context;
			this._tasks = context.Tasks;
		}

		public IEnumerable<ProjectTask> GetAll() {
			return _tasks.AsNoTracking().ToList();
		}

		public ProjectTask GetById(int id) {
			return _tasks.SingleOrDefault(x => x.Id == id);
		}

		public void Add(ProjectTask item) {
			_tasks.Add(item);
		}

		public void Remove(ProjectTask item) {
			_tasks.Remove(item);
		}

		public void SaveChanges() {
			_context.SaveChanges();
		}
	}
}
