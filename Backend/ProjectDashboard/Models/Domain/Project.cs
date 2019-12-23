using ProjectDashboard.Models.Domain.Interfaces;
using System;
using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain {
	public class Project {
		public int Id { get; set; }

		public int TeamId { get; set; }
		public Team Team { get; set; }


		public ICollection<ProjectTask> Tasks { get; set; }

		public DateTime LastEdit { get; set; }


		public int OwnerId { get; set; }

		public Company Owner { get; set; }

		public IPerson ContactPerson { get; set; } 
	}
}
