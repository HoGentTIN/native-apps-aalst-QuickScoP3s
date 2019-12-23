using ProjectDashboard.Models.Domain;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectDashboard.DTO {
	public class CompanyDTO {

		public int Id { get; set; }

		[Required]
		public string Name { get; set; }

		[Required]
		public string StreetNo { get; set; }

		[Required]
		public string StreetName { get; set; }

		[Required]
		public string ZipCode { get; set; }

		[Required]
		public string City { get; set; }

		public string State { get; set; }

		[Required]
		public string Country { get; set; }

		public void UpdateFromModel(Company company) {
			company.Name = this.Name;
			company.Address =
				string.IsNullOrWhiteSpace(this.State) ?
				string.Format("{0} {1}, {2} {3}, {4}", StreetName, StreetNo, ZipCode, City, Country) :
				string.Format("{0} {1}, {2} {3}, {4} {5}", StreetName, StreetNo, ZipCode, City, State, Country);
		}
	}
}
