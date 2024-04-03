defmodule Raindrops do
  def problem do
    %{
      special_cases: [
        %{divisor: 2, base_output: "pling"},
        %{divisor: 3, base_output: "plang"},
        %{divisor: 5, base_output: "plong"},
        %{divisor: 17, base_output: "tshäng"}
      ]
    }
  end

  def special_cases do
    problem()[:special_cases]
  end

  def divisible(n, divisor) do
    rem(n, divisor) == 0
  end

  def add_divisible_field(n, special_case) do
    d? = divisible(n, special_case[:divisor])
    case_with_divisible = Map.put(special_case, :divisible, d?)

    how_many_times =
      if d? do
        div(n, special_case[:divisor])
      else
        0
      end

    Map.put(case_with_divisible, :times_divisible, how_many_times)
  end

  def add_divisible_fields(n, special_cases) do
    special_cases
    |> Enum.map(&add_divisible_field(n, &1))
    |> Enum.sort_by(&Map.get(&1, :divisor))
  end

  def divisible_cases(n, special_cases) do
    add_divisible_fields(n, special_cases)
    |> Enum.filter(&Map.get(&1, :divisible))
    |> Enum.map(&Map.drop(&1, [:divisible]))
  end

  def first_transformation(s), do: String.upcase(s)
  def second_transformation(s), do: "*" <> first_transformation(s) <> "*"
  def third_transformation(s), do: Enum.join([second_transformation(s), s], " ")

  def transform_answer(%{base_output: base_output, times_divisible: times_divisible}) do
    case times_divisible do
      1 -> %{output: Function.identity(base_output)}
      2 -> %{output: first_transformation(base_output)}
      3 -> %{output: second_transformation(base_output)}
      _ -> %{output: third_transformation(base_output)}
    end
  end

  def raindrops(n), do: raindrops(n, problem())

  def raindrops(n, problem_map) do
    case divisible_cases(n, problem_map[:special_cases]) do
      [] ->
        "blob"

      answers ->
        transformed_answers =
          answers
          |> Enum.map(&transform_answer/1)
          |> Enum.map(&Map.get(&1, :output))

        Enum.join(transformed_answers, ", ")
    end
  end
end
