namespace ProjectDashboard.Models.Domain {
	public class ProjectTask {

		public int Id { get; set; }

		public string Description { get; set; }


		public int ProjectId { get; set; }

		public Project Project { get; set; }


		public int? AssigneeId { get; set; }

		public User Assignee { get; set; }

	}
}
