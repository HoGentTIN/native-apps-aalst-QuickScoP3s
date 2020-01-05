using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using NSwag;
using NSwag.Generation.Processors.Security;
using ProjectDashboard.Data;
using ProjectDashboard.Data.Repositories;
using ProjectDashboard.Models.Domain;
using ProjectDashboard.Models.Domain.Repositories;

namespace ProjectDashboard {
	public class Startup {

		public Startup(IConfiguration configuration) {
			Configuration = configuration;
		}

		public IConfiguration Configuration { get; }

		public void ConfigureServices(IServiceCollection services) {

			services.AddDbContext<ApplicationDbContext>(options =>
				options.UseMySql(Configuration.GetConnectionString("DefaultConnection")));

			services.AddIdentity<User, IdentityRole<int>>()
				.AddEntityFrameworkStores<ApplicationDbContext>()
				.AddDefaultTokenProviders();

			services.AddOpenApiDocument(c => {
				c.DocumentName = "apidocs";
				c.Title = "Project Dashboard API";
				c.Version = "v1";
				c.Description = "";
				c.DocumentProcessors.Add(new SecurityDefinitionAppender("JWT Token", new OpenApiSecurityScheme {
					Type = OpenApiSecuritySchemeType.ApiKey,
					Name = "Authorization",
					In = OpenApiSecurityApiKeyLocation.Header,
					Description = "Copy 'Bearer' + valid JWT token into field"
				}));
				c.OperationProcessors.Add(new OperationSecurityScopeProcessor("JWT Token"));
			});

			services.AddAuthorization(options => {
				options.AddPolicy("Admin", policy => {
					policy.RequireClaim(ClaimTypes.Role, "Admin");
				});

				options.AddPolicy("CompanyAdmin", policy => {
					policy.RequireClaim(ClaimTypes.Role, "CompanyAdmin");
				});

				options.AddPolicy("User", policy => {
					policy.AuthenticationSchemes.Add(JwtBearerDefaults.AuthenticationScheme);
					policy.RequireAuthenticatedUser();
				});

				options.DefaultPolicy = options.GetPolicy("User");
			});

			services.AddCors(options => options.AddPolicy("AllowedOrigins", builder => {
				builder
					.AllowAnyOrigin()
					.AllowAnyHeader()
					.AllowAnyMethod();
			}));

			services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
			.AddJwtBearer(options => {
				options.TokenValidationParameters = new TokenValidationParameters {
					ValidateIssuer = true,
					ValidateAudience = true,
					ValidateLifetime = true,
					ValidateIssuerSigningKey = true,
					ValidIssuer = Configuration["Jwt:Issuer"],
					ValidAudience = Configuration["Jwt:Issuer"],
					IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Configuration["Jwt:Key"]))
				};
			});

			services.Configure<IdentityOptions>(options => {
				// Password settings
				options.Password.RequiredLength = 6;
				options.Password.RequireDigit = false;
				options.Password.RequireLowercase = false;
				options.Password.RequireUppercase = false;
				options.Password.RequireNonAlphanumeric = false;

				// Lockout settings
				options.Lockout.DefaultLockoutTimeSpan = TimeSpan.FromMinutes(60);
				options.Lockout.MaxFailedAccessAttempts = 10;

				// User settings
				options.User.AllowedUserNameCharacters =
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._@+";
				options.User.RequireUniqueEmail = true;
			});

			services.AddScoped<AppDataInitializer>();
			services.AddScoped<ICompanyRepository, CompanyRepository>();
			services.AddScoped<IProjectRepository, ProjectRepository>();
			services.AddScoped<IBaseRepository<ProjectTask>, TasksRepository>();
			services.AddScoped<IBaseRepository<Team>, TeamsRepository>();
			services.AddScoped<IBaseRepository<User>, UserRepository>();

			services.AddMvc()
				.SetCompatibilityVersion(CompatibilityVersion.Version_2_2)
				.AddJsonOptions(options => {
					options.SerializerSettings.PreserveReferencesHandling = Newtonsoft.Json.PreserveReferencesHandling.Objects;
					options.SerializerSettings.DateFormatHandling = Newtonsoft.Json.DateFormatHandling.IsoDateFormat;
				});
		}

		public void Configure(IApplicationBuilder app, IHostingEnvironment env, AppDataInitializer initializer) {
			if (env.IsDevelopment()) {
				app.UseDeveloperExceptionPage();
				app.UseDatabaseErrorPage();
			}
			else {
				app.UseStatusCodePagesWithReExecute("/error/{0}");
				app.UseHsts();
			}

			app.UseCors("AllowedOrigins");

			app.UseHttpsRedirection();
			app.UseStaticFiles();
			app.UseAuthentication();

			app.UseRequestLocalization(app.ApplicationServices.GetService<IOptions<RequestLocalizationOptions>>().Value);

			app.UseMvcWithDefaultRoute();
			app.UseSwaggerUi3();
			app.UseOpenApi();

			initializer.InitializeData(env.IsDevelopment()).Wait();
		}
	}
}
