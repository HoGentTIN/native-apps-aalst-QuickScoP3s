using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using ProjectDashboard.Models.Domain;

namespace ProjectDashboard.Data.Mapping {
	public class UserConfiguration : IEntityTypeConfiguration<User> {
		public void Configure(EntityTypeBuilder<User> builder) {
			builder.ToTable("User");
			builder.HasKey(x => x.Id);

			builder.Property(x => x.FirstName).HasMaxLength(25).IsRequired();
			builder.Property(x => x.LastName).HasMaxLength(25).IsRequired();

			builder.Property(x => x.UserName).HasMaxLength(50).IsRequired();
			builder.Property(x => x.NormalizedUserName).HasMaxLength(50).IsRequired();

			builder.Property(x => x.Email).HasMaxLength(50).IsRequired();
			builder.Property(x => x.NormalizedEmail).HasMaxLength(50).IsRequired();

			builder.Property(x => x.PhoneNumber).HasMaxLength(15).IsRequired();
			builder.Property(x => x.PasswordHash).HasMaxLength(128).IsRequired();
			builder.Property(x => x.SecurityStamp).IsRequired();

			builder.Ignore(x => x.PhoneNumberConfirmed);
			builder.Ignore(x => x.TwoFactorEnabled);
			builder.Ignore(x => x.ConcurrencyStamp);
			builder.Ignore(x => x.EmailConfirmed);

			builder.Ignore(x => x.LockoutEnabled);
			builder.Ignore(x => x.LockoutEnd);
			builder.Ignore(x => x.AccessFailedCount);

			// Identity
			builder.HasMany<IdentityUserClaim<int>>().WithOne().HasForeignKey(uc => uc.UserId).IsRequired().OnDelete(DeleteBehavior.Cascade);
			builder.HasMany<IdentityUserLogin<int>>().WithOne().HasForeignKey(ul => ul.UserId).IsRequired().OnDelete(DeleteBehavior.Cascade);
			builder.HasMany<IdentityUserToken<int>>().WithOne().HasForeignKey(ut => ut.UserId).IsRequired().OnDelete(DeleteBehavior.Cascade);
		}
	}
}
