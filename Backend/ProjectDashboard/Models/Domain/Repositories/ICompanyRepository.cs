using System.Collections.Generic;

namespace ProjectDashboard.Models.Domain.Repositories {
	public interface ICompanyRepository: IBaseRepository<Company> {

		IEnumerable<User> GetUsersFromCompany(int companyId);

	}
}
