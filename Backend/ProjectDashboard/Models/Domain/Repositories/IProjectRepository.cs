using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain.Repositories {
	public interface IProjectRepository: IBaseRepository<Project> {

		bool NameExists(int ownerId, string name);

		IEnumerable<ProjectTask> GetTasksForProject(int projectId);

	}
}
