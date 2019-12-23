using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectDashboard.Models.Domain.Interfaces {
	public interface IPerson {

		int Id { get; set; }

		string FirstName { get; set; }

		string LastName { get; set; }

		string Email { get; set; }

		string PhoneNumber { get; set; }

		int CompanyId { get; set; }

		Company Company { get; set; }

	}
}
