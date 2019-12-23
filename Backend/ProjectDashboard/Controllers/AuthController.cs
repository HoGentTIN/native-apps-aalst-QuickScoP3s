using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using ProjectDashboard.DTO.Identity;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace ProjectDashboard.Controllers {

	[ApiConventionType(typeof(DefaultApiConventions))]
	[Produces("application/json")]
	[Route("api/[controller]")]
	[ApiController]
	public class AuthController : ControllerBase {

		private readonly SignInManager<User> _signInManager;
		private readonly UserManager<User> _userManager;
		private readonly IBaseRepository<Company> _companyRepo;

		private readonly IConfiguration _config;

		public AuthController(
			SignInManager<User> signInManager,
			UserManager<User> userManager,
			IBaseRepository<Company> companyRepo,
			IConfiguration config) {

			this._signInManager = signInManager;
			this._userManager = userManager;
			this._companyRepo = companyRepo;
			this._config = config;
		}

		// POST: api/auth/login
		[HttpPost("login")]
		public async Task<IActionResult> PostLogin(LoginDTO model) {
			User user = await _userManager.FindByEmailAsync(model.Email);
			if (user != null) {
				Microsoft.AspNetCore.Identity.SignInResult result = await _signInManager.CheckPasswordSignInAsync(user, model.Password, false);
				if (result.Succeeded) {
					string token = await GetTokenAsync(user);

					return Ok(new {
						user.Picture,
						Token = token
					});
				}
			}

			return Unauthorized("Invalid email or password");
		}

		private async Task<string> GetTokenAsync(User user) {
			IList<Claim> roles = await _userManager.GetClaimsAsync(user);

			Claim[] userclaims = new[]
			{
			  new Claim("sub", user.Id.ToString()), // User Id
			  new Claim("email", user.Email),
			  new Claim("given_name", user.FirstName),
			  new Claim("family_name", user.LastName),
			  new Claim("phone_number", user.PhoneNumber),
			  
			  new Claim(ClaimTypes.NameIdentifier, user.Id.ToString())
			};

			List<Claim> claims = new List<Claim>();
			claims.AddRange(userclaims);
			claims.AddRange(roles);

			SymmetricSecurityKey key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config["Jwt:Key"]));
			SigningCredentials creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

			JwtSecurityToken token = new JwtSecurityToken(
			  _config["Jwt:Issuer"],
			  _config["Jwt:Issuer"],
			  claims,
			  expires: DateTime.Now.AddMinutes(120),
			  signingCredentials: creds);

			return new JwtSecurityTokenHandler().WriteToken(token);
		}

		// POST: api/auth/refreshtoken
		[HttpPost]
		[Authorize]
		[Route("refreshtoken")]
		public async Task<IActionResult> RefreshTokenAsync() {
			User u = await _userManager.GetUserAsync(User);
			string token = await GetTokenAsync(u);

			return Ok(new {
				u.Picture,
				Token = token
			});
		}

		// POST: api/auth/register
		[HttpPost("register")]
		public async Task<IActionResult> PostRegister(RegisterDTO model) {
			User user = new User {
				Picture = model.Picture,
				FirstName = model.FirstName,
				LastName = model.LastName,
				Email = model.Email,
				PhoneNumber = model.PhoneNumber
			};

			if (model.CompanyId.HasValue) {
				var company = _companyRepo.GetById(model.CompanyId.Value);
				if (company != null)
					user.Company = company;
			}

			IdentityResult result = await _userManager.CreateAsync(user, model.Password);
			if (result.Succeeded) {
				string token = await GetTokenAsync(user);
				return Ok(new {
					user.Picture,
					Token = token
				});
			}

			return BadRequest("Registration failed");
		}

		// POST: api/auth/logout
		[Authorize]
		[HttpPost("logout")]
		public async Task<IActionResult> PostLogout() {
			await _signInManager.SignOutAsync();
			return Ok();
		}
	}
}