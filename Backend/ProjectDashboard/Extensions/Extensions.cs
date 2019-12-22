using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Dynamic;
using System.Globalization;
using System.Linq;
using System.Reflection;
using System.Text.RegularExpressions;

namespace ProjectDashboard.Extensions {
	public static class Extensions {

		public static bool DynamicPropertyExists(dynamic @object, string propertyName) {
			if (@object is ExpandoObject)
				return ((IDictionary<string, object>) @object).ContainsKey(propertyName);

			return @object.GetType().GetProperty(propertyName) != null;
		}

		public static bool EqualsIgnoreCase(this string input, string value) {
			return input.Equals(value, StringComparison.OrdinalIgnoreCase);
		}

		public static string Titleize(this string text) {
			return CultureInfo.CurrentCulture.TextInfo.ToTitleCase(text).ToSentenceCase();
		}

		public static string ToSentenceCase(this string str) {
			return Regex.Replace(str, "[a-z][A-Z]", m => m.Value[0] + " " + char.ToLower(m.Value[1]));
		}

		public static bool InclusiveBetween(this IComparable a, IComparable b, IComparable c) {
			return a.CompareTo(b) >= 0 && a.CompareTo(c) <= 0;
		}

		public static bool ExclusiveBetween(this IComparable a, IComparable b, IComparable c) {
			return a.CompareTo(b) > 0 && a.CompareTo(c) < 0;
		}
	}

	public static class EnumHelper<T> {

		public static T Parse(string value) {
			return GetValues().Single(x => GetDisplayValue(x).Equals(value));
		}

		public static IList<T> GetValues() {
			List<T> enumValues = new List<T>();

			foreach (FieldInfo fi in typeof(T).GetFields(BindingFlags.Static | BindingFlags.Public)) {
				enumValues.Add((T) Enum.Parse(typeof(T), fi.Name, false));
			}

			return enumValues;
		}

		public static string GetDisplayValue(T value) {
			FieldInfo fieldInfo = value.GetType().GetField(value.ToString());

			DisplayAttribute[] descriptionAttributes = fieldInfo.GetCustomAttributes(
				typeof(DisplayAttribute), false) as DisplayAttribute[];

			if (descriptionAttributes[0].ResourceType != null)
				return LookupResource(descriptionAttributes[0].ResourceType, descriptionAttributes[0].Name);

			if (descriptionAttributes == null) return string.Empty;
			return descriptionAttributes.Length > 0 ? descriptionAttributes[0].Name : value.ToString();
		}

		private static string LookupResource(Type resourceManagerProvider, string resourceKey) {
			foreach (PropertyInfo staticProperty in resourceManagerProvider.GetProperties(BindingFlags.Static | BindingFlags.NonPublic | BindingFlags.Public)) {
				if (staticProperty.PropertyType == typeof(System.Resources.ResourceManager)) {
					System.Resources.ResourceManager resourceManager = (System.Resources.ResourceManager) staticProperty.GetValue(null, null);
					return resourceManager.GetString(resourceKey);
				}
			}

			return resourceKey; // Fallback with the key name
		}
	}
}
