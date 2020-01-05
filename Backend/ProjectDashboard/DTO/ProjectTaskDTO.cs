using ProjectDashboard.Models.Domain;
using System.ComponentModel.DataAnnotations;

namespace ProjectDashboard.DTO {
	public class ProjectTaskDTO : IDTO<ProjectTask> {

		public int Id { get; set; }

		[Required]
		public string Title { get; set; }

		// Can be empty (Like a Github Issue)
		public string Description { get; set; } = "";

		public bool IsFinished { get; set; } = false;

		[Required]
		public int ProjectId { get; set; }

		public int? AssigneeId { get; set; }

		public void UpdateFromModel(ProjectTask item) {
			item.Title = this.Title;
			item.Description = this.Description ?? ""; // Should always at least be an empty string
			item.IsFinished = this.IsFinished;
			item.ProjectId = this.ProjectId;
			item.AssigneeId = this.AssigneeId;
		}
	}
}
