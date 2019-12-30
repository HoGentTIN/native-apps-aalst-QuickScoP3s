using Microsoft.EntityFrameworkCore;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;
using System.Collections.Generic;
using System.Linq;

namespace ProjectDashboard.Data.Repositories {
	public class TeamsRepository : IBaseRepository<Team> {

		private readonly ApplicationDbContext _context;
		private readonly DbSet<Team> _teams;

		public TeamsRepository(ApplicationDbContext context) {
			this._context = context;
			this._teams = context.Teams;
		}

		public IEnumerable<Team> GetAll() {
			return _teams.AsNoTracking()
				.Include(x => x.Lead)
				.Include(x => x.Members).ThenInclude(x => x.Member)
				.ToList();
		}

		public Team GetById(int id) {
			return _teams
				.Include(x => x.Lead)
				.Include(x => x.Members).ThenInclude(x => x.Member)
				.SingleOrDefault(x => x.Id == id);
		}

		public void Add(Team item) {
			_teams.Add(item);
		}

		public void Remove(Team item) {
			_teams.Remove(item);
		}

		public void SaveChanges() {
			_context.SaveChanges();
		}
	}
}
