using ProjectDashboard.Models.Domain;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace ProjectDashboard.DTO {
	public class TeamDTO : IDTO<Team> {

		public int Id { get; set; }

		[Required]
		public string Name { get; set; }

		[Required]
		public int LeadId { get; set; }

		[Required]
		public IEnumerable<int> MemberIds { get; set; }

		public void UpdateFromModel(Team item) {
			item.Name = this.Name;
			item.LeadId = this.LeadId;
		}
	}
}
