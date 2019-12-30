
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
				Company company1 = new Company { Name = "Microsoft", Address = "Microsoft Way 1, 98052 Redmond, Washington, USA" };
				Company company2 = new Company { Name = "Google", Address = "Amphitheater Parkway 1600, 94043 Mountain View, California, USA" };
				Company company3 = new Company { Name = "Apple", Address = "Infinite Loop 1, 95014 Cupertino, California, USA" };
				Company company4 = new Company { Name = "Amazon", Address = "12th Avenue South 1200, 98144 Seattle, Washington, USA" };
				Company company5 = new Company { Name = "RealDolmen", Address = "A. Vaucampslaan 42, 1654 Huizingen, Belgium" };
				Company company6 = new Company { Name = "InThePocket", Address = "Sassevaartstraat 46/401, 9000 Ghent, Belgium" };

				_context.Companies.AddRange(company1, company2, company3, company4, company5, company6);
				_context.SaveChanges();


				User demoUser = new User("Demo", "User", "demo.user@realdolmen.be", "+32400000000", company5);
				await _userManager.CreateAsync(demoUser, "DemoPassword");

				Team team = new Team("Team Project X", demoUser);
				_context.Teams.AddRange(team);
				_context.SaveChanges();

				Project project = new Project("Project X", team, company5, new ContactInfo("Antony", "Contact", "antonyc@realdolmen.be", "+32400000000"));
				project.Tasks.Add(new ProjectTask("Create the project", project, demoUser));
				project.Tasks.Add(new ProjectTask("Add initial views", project));
				project.Tasks.Add(new ProjectTask("Add initial models", project));
				project.Tasks.Add(new ProjectTask("Add initial viewmodels", project));

				_context.Projects.AddRange(project);
				_context.SaveChanges();
			}
		}
	}
}
