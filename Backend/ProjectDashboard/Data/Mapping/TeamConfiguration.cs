using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using ProjectDashboard.Models.Domain;

namespace ProjectDashboard.Data.Mapping {
	public class TeamConfiguration : IEntityTypeConfiguration<Team> {
		public void Configure(EntityTypeBuilder<Team> builder) {
			builder.ToTable("Teams");
			builder.HasKey(x => x.Id);

			builder
				.HasOne(x => x.Lead)
				.WithMany()
				.HasForeignKey(x => x.LeadId)
				.OnDelete(DeleteBehavior.Restrict);
		}
	}
}
