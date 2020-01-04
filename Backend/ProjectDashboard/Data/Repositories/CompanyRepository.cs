using Microsoft.EntityFrameworkCore;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;
using System.Collections.Generic;
using System.Linq;

namespace ProjectDashboard.Data.Repositories {
	public class CompanyRepository : ICompanyRepository {

		private readonly ApplicationDbContext _context;
		private readonly DbSet<Company> _companies;
		private readonly DbSet<User> _users;

		public CompanyRepository(ApplicationDbContext context) {
			this._context = context;
			this._companies = context.Companies;
			this._users = context.Users;
		}

		public IEnumerable<Company> GetAll() {
			return _companies.AsNoTracking().ToList();
		}

		public Company GetById(int id) {
			return _companies.SingleOrDefault(x => x.Id == id);
		}

		public IEnumerable<User> GetUsersFromCompany(int companyId) {
			return _users.AsNoTracking().Where(x => x.CompanyId == companyId).ToList();
		}

		public void Add(Company item) {
			_companies.Add(item);
		}

		public void Remove(Company item) {
			_companies.Remove(item);
		}

		public void SaveChanges() {
			_context.SaveChanges();
		}
	}
}
