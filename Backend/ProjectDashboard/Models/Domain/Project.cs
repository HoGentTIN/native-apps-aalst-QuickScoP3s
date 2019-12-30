using System;
using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain {
	public class Project {

		public int Id { get; set; }
		public string Name { get; set; }

		public int TeamId { get; set; }
		public Team Team { get; set; }


		public ICollection<ProjectTask> Tasks { get; set; } = new List<ProjectTask>();

		public DateTime LastEdit { get; set; }


		public int OwnerId { get; set; }

		public Company Owner { get; set; }

		public ContactInfo ContactPerson { get; set; }

		public Project() { }

		public Project(string name, Team team, Company owner, ContactInfo contact) {
			this.Name = name;
			this.Team = team;
			this.Owner = owner;
			this.ContactPerson = contact;
		}
	}
}
