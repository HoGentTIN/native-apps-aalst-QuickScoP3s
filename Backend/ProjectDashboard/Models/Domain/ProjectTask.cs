namespace ProjectDashboard.Models.Domain {
	public class ProjectTask {

		public int Id { get; set; }

		public string Description { get; set; }


		public int ProjectId { get; set; }

		public Project Project { get; set; }


		public int? AssigneeId { get; set; }

		public User Assignee { get; set; }

		public ProjectTask() { }

		public ProjectTask(string description, Project project, User assignee = null) {
			this.Description = description;
			this.Project = project;
			this.Assignee = assignee;
		}
	}
}
