using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using ProjectDashboard.Models.Domain;

namespace ProjectDashboard.Data.Mapping {
	public class ProjectConfiguration : IEntityTypeConfiguration<Project> {
		public void Configure(EntityTypeBuilder<Project> builder) {
			builder.ToTable("Projects");
			builder.HasKey(x => x.Id);

			builder
				.HasOne(x => x.Team)
				.WithMany()
				.HasForeignKey(x => x.TeamId)
				.OnDelete(DeleteBehavior.Cascade);

			builder
				.HasMany(x => x.Tasks)
				.WithOne()
				.OnDelete(DeleteBehavior.Cascade);

			builder
				.HasOne(x => x.Owner)
				.WithMany()
				.HasForeignKey(x => x.OwnerId);

			builder
				.HasOne(x => x.ContactPerson)
				.WithMany()
				.OnDelete(DeleteBehavior.Restrict);
		}
	}
}
