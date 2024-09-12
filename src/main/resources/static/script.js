const API_URL = "https://66c716bd732bf1b79fa54c21.mockapi.io/api/v1";


const dailyBtn = document.querySelector(".daily-para");
const weeklyBtn = document.querySelector(".weekly-para");
const monthlyBtn = document.querySelector(".monthly-para");
const themeCardAll = document.querySelectorAll(".theme-card");
const cardWork = document.querySelector(".card-1");
const cardPlay = document.querySelector(".card-2");
const cardStudy = document.querySelector(".card-3");
const cardExercise = document.querySelector(".card-4");
const cardSocial = document.querySelector(".card-5");
const cardSelfCare = document.querySelector(".card-6");
const ellipsisIconWork = document.querySelector(".card-work");
const ellipsisIconPlay = document.querySelector(".card-play");
const ellipsisIconStudy = document.querySelector(".card-study");
const ellipsisIconExercise = document.querySelector(".card-exercise");
const ellipsisIconSocial = document.querySelector(".card-social");
const ellipsisIconSelfCare = document.querySelector(".card-self-care");
const dataCard = document.querySelectorAll(".data-card");
let paraCurrent = document.querySelectorAll("p.para-current");
let spanPrevious = document.querySelectorAll(".span-previous");

// request JSON data in async function //




async function requestDailyData() {
  const response = await fetch(`${API_URL}/daily`);
  if (!response.ok) throw new Error("Failed to fetch daily data");
  const data = await response.json();
  

  // daily button listener //
  dailyBtn.addEventListener("click", (event) => {
    themeCardAll.forEach((element) => {
      // iteration through all theme-cards //
      for (let i = 0; i < data.length; i++) {
        paraCurrent[i].textContent =
          data[i].daily_current +
          (data[i].daily_current > 1 ? "hrs" : "hr");
        spanPrevious[i].textContent =
          "Yesterday - " +
          data[i].daily_previous +
          (data[i].daily_previous > 1 ? "hrs" : "hr");
      }
    });
    changeActive(dailyBtn, weeklyBtn, monthlyBtn);
    event.preventDefault();
  });
}
requestDailyData();// call function for JSON data parse //



  // weekly button listener //
  async function requestWeeklyData() {
    const response = await fetch(`${API_URL}/monthly`);
    if (!response.ok) throw new Error("Failed to fetch weekly data");
    const data = await response.json();

  weeklyBtn.addEventListener("click", (event) => {
    themeCardAll.forEach((element) => {
      // iteration through all theme-cards //
      for (let i = 0; i < data.length; i++) {
        // iteration through all objects in json data string (could be replaced with forEach() for simplicity - untested)//
        paraCurrent[i].textContent = data[i].monthly_current + "hrs";
        spanPrevious[i].textContent =
          "Last Week - " + data[i].monthly_previous + "hrs";
      }
    });
    changeActive(weeklyBtn, monthlyBtn, dailyBtn);
    event.preventDefault();
  });
  }
  requestWeeklyData();// call function for JSON data parse //



  // monthly button listener //
  async function requestMonthlyData() {
    const response = await fetch(`${API_URL}/monthly`);
    if (!response.ok) throw new Error("Failed to fetch monthly data");
    const data = await response.json();
  monthlyBtn.addEventListener("click", (event) => {
    themeCardAll.forEach((element) => {
      // iteration through all theme-cards //
      for (let i = 0; i < data.length; i++) {
        paraCurrent[i].textContent = data[i].monthly_current + "hrs";
        spanPrevious[i].textContent =
          "Last Month - " + data[i].monthly_previous + "hrs";
      }
    });
    changeActive(monthlyBtn, weeklyBtn, dailyBtn);
    event.preventDefault();
  });
}
requestMonthlyData();// call function for JSON data parse //

//TO-DO change active butonlara tekrar bkacağız

// function that removes active class with white color text //
function changeActive(element, previousActive1, previousActive2) {
  previousActive1.classList.remove("active");
  previousActive2.classList.remove("active");
  element.classList.add("active");
}

// ellipsis img click eventlistener //

function extraInfo() {
  // variable for each card is needed in order to trigger open and close individually //
  let closedWork = false;
  let closedPlay = false;
  let closedStudy = false;
  let closedExercise = false;
  let closedSocial = false;
  let closedSelfCare = false;

  ellipsisIconWork.addEventListener("click", () => {
    // conditional for opening
    if (!closedWork) {
      cardWork.classList.remove("hidden");
      dataCard[0].classList.remove("border-radius");
      dataCard[0].classList.add("border-top-radius");
      closedWork = true;
      // or closing the extra tab
    } else {
      cardWork.classList.add("hidden");
      dataCard[0].classList.add("border-radius");
      dataCard[0].classList.remove("border-top-radius");
      closedWork = false;
    }
  });
  ellipsisIconPlay.addEventListener("click", () => {
    // conditional for opening
    if (!closedPlay) {
      cardPlay.classList.remove("hidden");
      dataCard[1].classList.remove("border-radius");
      dataCard[1].classList.add("border-top-radius");
      closedPlay = true;
      // or closing the extra tab
    } else {
      cardPlay.classList.add("hidden");
      dataCard[1].classList.add("border-radius");
      dataCard[1].classList.remove("border-top-radius");
      closedPlay = false;
    }
  });
  ellipsisIconStudy.addEventListener("click", () => {
    // conditional for opening
    if (!closedStudy) {
      cardStudy.classList.remove("hidden");
      dataCard[2].classList.remove("border-radius");
      dataCard[2].classList.add("border-top-radius");
      closedStudy = true;
      // or closing the extra tab
    } else {
      cardStudy.classList.add("hidden");
      dataCard[2].classList.add("border-radius");
      dataCard[2].classList.remove("border-top-radius");
      closedStudy = false;
    }
  });
  ellipsisIconExercise.addEventListener("click", () => {
    // conditional for opening
    if (!closedExercise) {
      cardExercise.classList.remove("hidden");
      dataCard[3].classList.remove("border-radius");
      dataCard[3].classList.add("border-top-radius");
      closedExercise = true;
      // or closing the extra tab
    } else {
      cardExercise.classList.add("hidden");
      dataCard[3].classList.add("border-radius");
      dataCard[3].classList.remove("border-top-radius");
      closedExercise = false;
    }
  });
  ellipsisIconSocial.addEventListener("click", () => {
    // conditional for opening
    if (!closedSocial) {
      cardSocial.classList.remove("hidden");
      dataCard[4].classList.remove("border-radius");
      dataCard[4].classList.add("border-top-radius");
      closedSocial = true;
      // or closing the extra tab
    } else {
      cardSocial.classList.add("hidden");
      dataCard[4].classList.add("border-radius");
      dataCard[4].classList.remove("border-top-radius");
      closedSocial = false;
    }
  });
  ellipsisIconSelfCare.addEventListener("click", () => {
    // conditional for opening
    if (!closedSelfCare) {
      cardSelfCare.classList.remove("hidden");
      dataCard[5].classList.remove("border-radius");
      dataCard[5].classList.add("border-top-radius");
      closedSelfCare = true;
      // or closing the extra tab
    } else {
      cardSelfCare.classList.add("hidden");
      dataCard[5].classList.add("border-radius");
      dataCard[5].classList.remove("border-top-radius");
      closedSelfCare = false;
    }
  });
}

extraInfo();

// tests to set time selection window to new heights //

// if (window.innerWidth > 1024) {
//   if (!closedTop && !closedBottom) {
//     console.log("this is time to open wide");
//   } else if (!closedTop) {
//     console.log("open top");
//   } else console.log("open bottom");
// }

// Workflow //

// eventlistener for timeframe (daily, weekly, monthly)
// populate each theme card with data.json file
//
// using forEach() to populate each theme-card with data isn't working. More research neeeded.
//
