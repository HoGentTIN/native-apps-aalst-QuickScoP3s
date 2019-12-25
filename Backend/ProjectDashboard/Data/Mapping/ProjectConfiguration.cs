using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Newtonsoft.Json;
using ProjectDashboard.Models.Domain;

namespace ProjectDashboard.Data.Mapping {
	public class ProjectConfiguration : IEntityTypeConfiguration<Project> {
		public void Configure(EntityTypeBuilder<Project> builder) {
			builder.ToTable("Projects");
			builder.HasKey(x => x.Id);

			builder.Property(x => x.ContactPerson).HasConversion(
				entity => JsonConvert.SerializeObject(entity),
				value => JsonConvert.DeserializeObject<ContactInfo>(value)
			);

			builder
				.HasOne(x => x.Team)
				.WithMany()
				.HasForeignKey(x => x.TeamId)
				.OnDelete(DeleteBehavior.Cascade);

			builder
				.HasMany(x => x.Tasks)
				.WithOne(x => x.Project)
				.HasForeignKey(x => x.ProjectId)
				.OnDelete(DeleteBehavior.Cascade);

			builder
				.HasOne(x => x.Owner)
				.WithMany()
				.HasForeignKey(x => x.OwnerId);
		}
	}
}
