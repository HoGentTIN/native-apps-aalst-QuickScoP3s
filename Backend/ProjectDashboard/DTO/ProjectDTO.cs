using ProjectDashboard.Models.Domain;
using System;
using System.ComponentModel.DataAnnotations;

namespace ProjectDashboard.DTO {
	public class ProjectDTO : IDTO<Project> {

		public int Id { get; set; }

		[Required]
		public string Name { get; set; }

		[Required]
		public ContactInfo ContactPerson { get; set; }

		public int? TeamId { get; set; }

		public int? OwnerId { get; set; }

		public DateTime? LastEdit { get; set; }

		public void UpdateFromModel(Project item) {
			item.Name = this.Name;
			item.ContactPerson = this.ContactPerson;
		}
	}
}
