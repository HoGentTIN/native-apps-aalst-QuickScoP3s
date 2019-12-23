using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using ProjectDashboard.Models.Domain.Interfaces;

namespace ProjectDashboard.Data.Mapping {
	public class IPersonConfiguration : IEntityTypeConfiguration<IPerson> {
		public void Configure(EntityTypeBuilder<IPerson> builder) {
			builder.ToTable("People");
			builder.HasKey(x => x.Id);

			builder.Property(x => x.FirstName).HasMaxLength(25).IsRequired();
			builder.Property(x => x.LastName).HasMaxLength(25).IsRequired();
			builder.Property(x => x.Email).HasMaxLength(50).IsRequired();
			builder.Property(x => x.PhoneNumber).HasMaxLength(15).IsRequired();

			builder
				.HasOne(x => x.Company)
				.WithMany()
				.HasForeignKey(x => x.CompanyId)
				.IsRequired(false)
				.OnDelete(DeleteBehavior.Cascade);
		}
	}
}
