using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectDashboard.Models.Domain.Repositories {
	public interface IProjectRepository: IBaseRepository<Project> {

		bool NameExists(string name);

	}
}
