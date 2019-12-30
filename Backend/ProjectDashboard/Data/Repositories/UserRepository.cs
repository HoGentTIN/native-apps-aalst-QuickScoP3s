using Microsoft.EntityFrameworkCore;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;

namespace ProjectDashboard.Data.Repositories {
	public class UserRepository : IBaseRepository<User> {

		private readonly ApplicationDbContext _context;
		private readonly DbSet<User> _users;

		public UserRepository(ApplicationDbContext context) {
			this._context = context;
			this._users = context.Users;
		}

		public IEnumerable<User> GetAll() {
			return _users.AsNoTracking().ToList();
		}

		public User GetById(int id) {
			return _users.SingleOrDefault(x => x.Id == id);
		}

		public void Add(User item) {
			throw new NotSupportedException();
		}

		public void Remove(User item) {
			throw new NotSupportedException();
		}

		public void SaveChanges() {
			_context.SaveChanges();
		}
	}
}
