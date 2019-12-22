
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using ProjectDashboard.Models.Domain;

namespace ProjectDashboard.Data {
	public class ApplicationDbContext : IdentityDbContext<User, IdentityRole<int>, int> {

		public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options) { }

		protected override void OnModelCreating(ModelBuilder builder) {
			base.OnModelCreating(builder);

			//TODO Add Configs

			builder.Entity<IdentityRole<int>>(b => {
				b.ToTable("Roles");

				b.HasMany<IdentityRole<int>>().WithOne().HasForeignKey(ur => ur.Id).IsRequired().OnDelete(DeleteBehavior.Cascade);
				b.HasMany<IdentityRoleClaim<int>>().WithOne().HasForeignKey(rc => rc.Id).IsRequired().OnDelete(DeleteBehavior.Cascade);
			});

			builder.Entity<IdentityRoleClaim<int>>(b => {
				b.ToTable("Roles/Claims");
			});

			builder.Entity<IdentityUserClaim<int>>(b => {
				b.ToTable("User/Claims");
			});

			builder.Entity<IdentityUserRole<int>>(b => {
				b.ToTable("User/Roles");
			});

			builder.Entity<IdentityUserToken<int>>(b => {
				b.ToTable("User/Tokens");
			});

			builder.Entity<IdentityUserLogin<int>>(b => {
				b.ToTable("User/Login");
			});
		}
	}
}
