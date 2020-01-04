namespace ProjectDashboard.Models.Domain.Repositories {
	public interface IProjectRepository: IBaseRepository<Project> {

		bool NameExists(int ownerId, string name);

	}
}
