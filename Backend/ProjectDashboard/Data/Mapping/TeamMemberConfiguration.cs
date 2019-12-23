using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using ProjectDashboard.Models.Domain;

namespace ProjectDashboard.Data.Mapping {
	public class TeamMemberConfiguration : IEntityTypeConfiguration<TeamMember> {
		public void Configure(EntityTypeBuilder<TeamMember> builder) {
			builder.ToTable("TeamMembers");
			builder.HasKey(x => new { x.TeamId, x.MemberId });

			builder
				.HasOne(x => x.Member)
				.WithMany(x => x.Teams)
				.HasForeignKey(x => x.MemberId);

			builder
				.HasOne(x => x.Team)
				.WithMany(x => x.Members)
				.HasForeignKey(x => x.TeamId);
		}
	}
}
