
using Microsoft.AspNetCore.Identity;
using ProjectDashboard.Models.Domain;
using System;
using System.Threading.Tasks;

namespace ProjectDashboard.Data {
	public class AppDataInitializer {

		private readonly Random RND = new Random();

		private readonly ApplicationDbContext _context;
		private readonly UserManager<User> _userManager;

		public AppDataInitializer(ApplicationDbContext context, UserManager<User> userManager) {
			_context = context;
			_userManager = userManager;
		}

		public async Task InitializeData(bool isDev) {
			//if (isDev) -> Voor development op azure mag bij elke herstart de db vernieuwen
			_context.Database.EnsureDeleted();

			if (_context.Database.EnsureCreated()) {
				//TODO Add Configs
			}
		}
	}
}
