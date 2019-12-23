using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ProjectDashboard.Models.Domain.Repositories {
	public interface IBaseRepository<T> {

		IEnumerable<T> GetAll();

		T GetById(int id);

		void Add(T item);

		void Remove(T item);

		void SaveChanges();

	}
}
